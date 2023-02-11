rsync -avzhe ssh --progress -m --include="*/" --include="*."{bcl,bcl.*} --exclude="*" "$1" "$2"
echo "$1"
echo "$2"