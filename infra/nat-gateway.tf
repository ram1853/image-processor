resource "aws_nat_gateway" "image-processor-ng" {
  allocation_id = aws_eip.elastic-ip.id
  subnet_id     = aws_subnet.public-subnet-1.id

  tags = {
    Name = "image-processor-ng"
  }

  # To ensure proper ordering, it is recommended to add an explicit dependency
  # on the Internet Gateway for the VPC.
  depends_on = [aws_internet_gateway.image-processor-ig]
}

resource "aws_eip" "elastic-ip" {
  domain = "vpc"
}