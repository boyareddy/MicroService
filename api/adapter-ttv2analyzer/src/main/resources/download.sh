rsync -avzhe ssh --progress -m --include="*/" --include="*."{vcf,vcf.*} --exclude="*" "$1" "$2"
echo "$1"
echo "$2"