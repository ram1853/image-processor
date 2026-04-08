resource "aws_route_table" "private-route-table" {
  vpc_id = aws_vpc.image-processor-vpc.id

  route {
    cidr_block = "10.0.0.0/16"
    gateway_id = "local"
  }

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_nat_gateway.image-processor-ng.id
  }

  tags = {
    Name = "private-route-table"
  }
}

resource "aws_route_table" "public-route-table" {
  vpc_id = aws_vpc.image-processor-vpc.id

  route {
    cidr_block = "10.0.0.0/16"
    gateway_id = "local"
  }

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.image-processor-ig.id
  }

  tags = {
    Name = "public-route-table"
  }
}

resource "aws_route_table_association" "private-route-table-association-1" {
  subnet_id      = aws_subnet.private-subnet-1.id
  route_table_id = aws_route_table.private-route-table.id
}

resource "aws_route_table_association" "private-route-table-association-2" {
  subnet_id      = aws_subnet.private-subnet-2.id
  route_table_id = aws_route_table.private-route-table.id
}

resource "aws_route_table_association" "public-route-table-association-2" {
  subnet_id      = aws_subnet.public-subnet-1.id
  route_table_id = aws_route_table.public-route-table.id
}