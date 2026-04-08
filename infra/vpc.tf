# cidr.xyz to see IP counts available for a cidr
resource "aws_vpc" "image-processor-vpc" {
  cidr_block           = "10.0.0.0/16"
  instance_tenancy     = "default"
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name = "image-processor-vpc"
  }
}