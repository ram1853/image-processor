#!/bin/zsh

userName=$1
fileName=$2
absoluteFilePath=$3

URL=$(curl -s -H "Content-Type: application/json" -d '{"userName": "'$userName'", "fileName": "'$fileName'"}' https://2z1je3tqk4.execute-api.ap-south-1.amazonaws.com/dev/upload-url | jq -r '.url')

echo "S3 Presigned URL:"
echo $URL

uploadStatus=$(curl -s -o /dev/null -w "%{http_code}" -H "Content-Type: image/jpg" -T $absoluteFilePath "$URL")

if [ "$uploadStatus" -eq 200 ]; then
    echo "Upload successful"
else
    echo "Upload failed (HTTP $uploadStatus)"
fi