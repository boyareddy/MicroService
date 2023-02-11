rsync -avzhe ssh --progress -m --checksum --include="*/" --include="*.bcl.*" --exclude="*" "$1" "$2"
echo "$1"
echo "$2"