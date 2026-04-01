data "aws_availability_zones" "available-azs" {
  state = "available"
}

resource "aws_subnet" "public-subnet-1" {
  vpc_id     = aws_vpc.image-processor-vpc.id
  cidr_block = "10.0.0.0/24"
  availability_zone = data.aws_availability_zones.available-azs.names[0]
  map_public_ip_on_launch = true

  tags = {
    Name = "public-subnet-1"
  }
}

resource "aws_subnet" "private-subnet-1" {
  vpc_id     = aws_vpc.image-processor-vpc.id
  cidr_block = "10.0.1.0/24"
  availability_zone = data.aws_availability_zones.available-azs.names[0]

  tags = {
    Name = "private-subnet-1"
    "kubernetes.io/cluster/test-cluster" = "shared"
    "kubernetes.io/role/internal-elb" = "1"
  }
}

resource "aws_subnet" "private-subnet-2" {
  vpc_id     = aws_vpc.image-processor-vpc.id
  cidr_block = "10.0.2.0/24"
  availability_zone = data.aws_availability_zones.available-azs.names[1]

  tags = {
    Name = "private-subnet-2"
    "kubernetes.io/cluster/test-cluster" = "shared"
    "kubernetes.io/role/internal-elb" = "1"
  }
}