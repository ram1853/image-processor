resource "aws_internet_gateway" "image-processor-ig" {
  vpc_id = aws_vpc.image-processor-vpc.id

  tags = {
    Name = "image-processor-ig"
  }
}