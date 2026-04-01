# image-processor
Backend Project that processes high definition images to medium and small formats.

TODO:
Write about how the s3-presigned url permissions work (you can generate the upload url even without the s3 put object permission),
however when using the url it will fail with 403. Concept is whoever is creating the url should have the s3 put object
permission, so that the upload is allowed (in this case lambda)
content-type should be used while uploading using the presigned url - else you get misleading errors like missing authentication token

Write about the VPC CIDR selection and dividing the ip address range across 4 subnets. (floors in a building with number
of rooms in each floor analogy by chatgpt). In each subnet 5 IP address is reserved for aws.

Consider a whole building as the VPC CIDR range (e.g /16) - which means 2 ^ (32-16) = 65,536 IPs
Now for /16 - last 2 octet can change.
If I choose my vpc cidr as 10.0.0.0/16, and if I want to have 4 subnets within this vpc - then the best recommended way
is to choose/allocate subset of the vpc cidr ip ranges while having some room empty for future.
For e.g.
Each subnet can have /24 subnet mask which means every subnet will have 2 ^ (32-24) = 256 IPs or to be precise
251 IPs (as 5 IPs are reserved for AWS). 251 IPs per subnet is a good starting point as it gives enough IPs for the
instances to scale within the subnet.
Now how do you choose the CIDR of each of these subnets?
Now the building can have many floors (0,1,2 etc.).

Subnet 1 - 10.0.0.0/24 = 251 IPs
Subnet 2 - 10.0.1.0/24 = 251 IPs
Subnet 3 - 10.0.2.0/24 = 251 IPs
Subnet 4 - 10.0.3.0/24 = 251 IPs

Each floor will have many rooms -> Here every room is an IP.
For e.g. for Subnet 1, it will have 256 IPs, but it can't have more than that, so we make the next jump,
which means we are going to the next floor which is Subnet 2 which again can have 256 IPs (or rooms).

Now don't get confused that why the 3rd octet is changing when the mask is /24.
This octet change should be considered only when you declare the CIDR,
for e.g. for Subnet 3 - 10.0.2.0/24 -> only the last octet can change potentially giving this subnet 256 IPs.

Now if you step back and visualize from the 'building' visual which is your vpc CIDR - This was declared with /16 mask,
which means the last 2 octet can change which is what is happening here (For every floor the 3rd octet is changing, and
in every floor the last octet is changing). [Building - VPC, Floor - subnet, Room - IP]

All worker nodes to be in private subnets only as our app is not internet facing.
No public subnets, igw and nat-gw needed as of now. Instead use vpc endpoint to consume from sqs.
Explore KEDA for auto-scaling.

aws eks cluster addons - vpc cni, kube proxy, core dns