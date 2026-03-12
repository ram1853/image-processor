# image-processor
Backend Project that processes high definition images to medium and small formats.

TODO:
Write about how the s3-presigned url permissions work (you can generate the upload url even without the s3 put object permission),
however when using the url it will fail with 403. Concept is whoever is creating the url should have the s3 put object
permission, so that the upload is allowed (in this case lambda)
content-type should be used while uploading using the presigned url - else you get misleading errors like missing authentication token
