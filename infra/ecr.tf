resource "aws_ecr_repository" "image-processor" {
  name                 = "image-processor"
  image_tag_mutability = "MUTABLE"
}