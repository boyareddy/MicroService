rsync -avzhe ssh --progress -m --include="*/" --include="RTAComplete.txt" --exclude="*" "$1" "$2"
echo "$1"
echo "$2"