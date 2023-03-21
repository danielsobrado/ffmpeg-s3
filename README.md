# FFmpeg File Conversion on S3 buckets

This is a project using FFmpeg to convert files from one format to another. The project reads and saves files to a Minio instance running in a Docker container.

## Prerequisites

- Java 17
- Gradle
- Docker
- Docker Compose
- Minio
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

## Using a RAM Disk for Temporary File Storage

By default, the application stores temporary files on disk during the file conversion process. To minimize the overhead of reading and writing files to disk, you can use a RAM disk as the temporary file storage.

### Creating a RAM Disk on Linux

To create a RAM disk on Linux, run the following commands:

```bash
sudo mkdir /mnt/ramdisk
sudo mount -t tmpfs -o size=1G tmpfs /mnt/ramdisk
```
This creates a RAM disk with a size of 1 GB mounted at `/mnt/ramdisk`. You can adjust the size according to your requirements.

## Configuring the Application to Use the RAM Disk
Update the tempDir property in your application.yml file to point to the RAM disk:

```
tempDir: /mnt/ramdisk
```

Keep in mind that using a RAM disk will consume memory, so ensure your system has enough free RAM to accommodate the disk size and other running processes.

## Testing
Run the tests for the project with the following command:

```bash
./gradlew test
```

## License
This project is licensed under the MIT License.
