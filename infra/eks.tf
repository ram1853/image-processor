resource "aws_eks_cluster" "image-processor-cluster" {
  name = "image-processor-cluster"

  access_config {
    authentication_mode = "API"
  }

  role_arn = aws_iam_role.image-processor-cluster-role.arn
  version  = "1.35"

  vpc_config {
    endpoint_private_access = true
    endpoint_public_access  = true
    subnet_ids = [
      aws_subnet.private-subnet-1.id,
      aws_subnet.private-subnet-2.id
    ]
  }

  depends_on = [
    aws_iam_role_policy_attachment.AmazonEKSClusterPolicy,
    aws_iam_role_policy_attachment.AmazonEKSBlockStoragePolicy,
    aws_iam_role_policy_attachment.AmazonEKSComputePolicy,
    aws_iam_role_policy_attachment.AmazonEKSLoadBalancingPolicy,
    aws_iam_role_policy_attachment.AmazonEKSNetworkingPolicy,
  ]
}

resource "aws_iam_role" "image-processor-cluster-role" {
  name = "image-processor-cluster-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "sts:AssumeRole",
          "sts:TagSession"
        ]
        Effect = "Allow"
        Principal = {
          Service = "eks.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_role_policy_attachment" "AmazonEKSClusterPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.image-processor-cluster-role.name
}

resource "aws_iam_role_policy_attachment" "AmazonEKSBlockStoragePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSBlockStoragePolicy"
  role       = aws_iam_role.image-processor-cluster-role.name
}

resource "aws_iam_role_policy_attachment" "AmazonEKSComputePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSComputePolicy"
  role       = aws_iam_role.image-processor-cluster-role.name
}

resource "aws_iam_role_policy_attachment" "AmazonEKSLoadBalancingPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSLoadBalancingPolicy"
  role       = aws_iam_role.image-processor-cluster-role.name
}

resource "aws_iam_role_policy_attachment" "AmazonEKSNetworkingPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSNetworkingPolicy"
  role       = aws_iam_role.image-processor-cluster-role.name
}

resource "aws_eks_addon" "vpc-cni" {
  addon_name   = "vpc-cni"
  cluster_name = aws_eks_cluster.image-processor-cluster.name
  addon_version = "v1.21.1-eksbuild.7"
  resolve_conflicts_on_create = "OVERWRITE"
}

resource "aws_eks_addon" "kube-proxy" {
  addon_name   = "kube-proxy"
  cluster_name = aws_eks_cluster.image-processor-cluster.name
  addon_version = "v1.35.3-eksbuild.2"
  resolve_conflicts_on_create = "OVERWRITE"
}

resource "aws_eks_addon" "coredns" {
  addon_name   = "coredns"
  cluster_name = aws_eks_cluster.image-processor-cluster.name
  addon_version = "v1.13.2-eksbuild.4"
  resolve_conflicts_on_create = "OVERWRITE"
}

resource "aws_eks_addon" "eks-pod-identity-agent" {
  addon_name   = "eks-pod-identity-agent"
  cluster_name = aws_eks_cluster.image-processor-cluster.name
  addon_version = "v1.3.10-eksbuild.2"
  resolve_conflicts_on_create = "OVERWRITE"
}

resource "aws_iam_role" "eks-node-group-role" {
  name = "eks-node-group-role"

  assume_role_policy = jsonencode({
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "ec2.amazonaws.com"
      }
    }]
    Version = "2012-10-17"
  })
}

resource "aws_iam_role_policy_attachment" "AmazonEKSWorkerNodePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
  role       = aws_iam_role.eks-node-group-role.name
}

resource "aws_iam_role_policy_attachment" "AmazonEKS_CNI_Policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
  role       = aws_iam_role.eks-node-group-role.name
}

resource "aws_iam_role_policy_attachment" "AmazonEC2ContainerRegistryReadOnly" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
  role       = aws_iam_role.eks-node-group-role.name
}

# Defaults to t3.medium type instance
resource "aws_eks_node_group" "node-group" {
  cluster_name    = aws_eks_cluster.image-processor-cluster.name
  node_group_name = "node-group"
  node_role_arn   = aws_iam_role.eks-node-group-role.arn
  subnet_ids      = [aws_subnet.private-subnet-1.id, aws_subnet.private-subnet-2.id]

  scaling_config {
    desired_size = 2
    max_size     = 2
    min_size     = 2
  }

  update_config {
    max_unavailable = 1
  }

  depends_on = [
    aws_iam_role_policy_attachment.AmazonEKSWorkerNodePolicy,
    aws_iam_role_policy_attachment.AmazonEKS_CNI_Policy,
    aws_iam_role_policy_attachment.AmazonEC2ContainerRegistryReadOnly
  ]
}

# Below two needed to interact with cluster from your machine using kubectl and to view nodes from console (provided you login to console using the Admin user defined below)
resource "aws_eks_access_entry" "admin-access" {
  cluster_name      = aws_eks_cluster.image-processor-cluster.name
  principal_arn     = "arn:aws:iam::545009866715:user/Admin"
  type              = "STANDARD"
}

resource "aws_eks_access_policy_association" "access-policy" {
  cluster_name  = aws_eks_cluster.image-processor-cluster.name
  policy_arn    = "arn:aws:eks::aws:cluster-access-policy/AmazonEKSClusterAdminPolicy"
  principal_arn = "arn:aws:iam::545009866715:user/Admin"

  access_scope {
    type       = "cluster"
  }
}

# Below role is for the apps running in pod
resource "aws_iam_role" "pod-identity-role" {
  name = "pod-identity-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "sts:AssumeRole",
          "sts:TagSession"
        ]
        Effect = "Allow"
        Principal = {
          Service = "pods.eks.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_policy" "pod-sqs-policy" {
  name = "pod-sqs-policy"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
        {
            Action  = ["sqs:ListQueues", "sqs:ReceiveMessage", "sqs:createqueue", "sqs:deletemessage"]
            Effect   = "Allow"
            Resource = "arn:aws:sqs:${data.aws_region.current.region}:${data.aws_caller_identity.current.account_id}:${aws_sqs_queue.sqs_queue.name}"
        }
    ]
  })
}

resource "aws_iam_policy" "pod-s3-policy" {
  name = "pod-s3-policy"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
        {
            Action  = ["s3:GetObject", "s3:PutObject"]
            Effect   = "Allow"
            Resource = "arn:aws:s3:::${var.s3-bucket-name}/*"
        }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "pod-sqs-policy-attachment" {
  policy_arn = aws_iam_policy.pod-sqs-policy.arn
  role       = aws_iam_role.pod-identity-role.name
}

resource "aws_iam_role_policy_attachment" "pod-s3-policy-attachment" {
  policy_arn = aws_iam_policy.pod-s3-policy.arn
  role       = aws_iam_role.pod-identity-role.name
}

resource "aws_eks_pod_identity_association" "image-processor-pod-identity-association" {
  cluster_name    = aws_eks_cluster.image-processor-cluster.name
  namespace       = "default"
  service_account = "image-processor-sa"
  role_arn        = aws_iam_role.pod-identity-role.arn
}