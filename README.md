# FFmpeg File Conversion on S3 buckets

This is a project using FFmpeg to convert files from one format to another. The project reads and saves files to a Minio instance running in a Docker container.

## Prerequisites

- JDK 11 or later
- Docker
- Docker Compose
- FFmpeg installed and accessible in system's `PATH`

## Setup

1. Clone the repository:

```bash
git clone https://github.com/danielsobrado/ffmpeg-s3.git
cd ffmpeg-s3
```
Build and run the project with Docker Compose:

```bash
docker-compose up --build
```

The Java application will be accessible at http://localhost:8080, and the Minio instance will be accessible at http://localhost:9000.

## Usage
The application exposes an API for file conversion. Send a POST request with the required parameters to the /convert endpoint to start a file conversion:

```bash
curl -X POST "http://localhost:8080/convert" \
     -H "Content-Type: application/json" \
     -d '{
           "inputBucket": "input-bucket",
           "outputBucket": "output-bucket",
           "inputFileKey": "path/to/input/file",
           "outputFileKey": "path/to/output/file",
           "inputFormat": "avi",
           "outputFormat": "mp4"
         }'
```
Replace the placeholders with the appropriate Minio bucket names, object keys, and file formats.

## Testing
Run the tests for the project with the following command:

```bash
./gradlew test
```

## License
This project is licensed under the MIT License.
