#!/bin/sh

dir="src"

for file in $(find "$dir" -name '*.kt'); do
	sed -i '/Log\.[a-z]\(.*\)\s*$/d' "$file" 
done

result="$(grep -RE 'Log\.[a-z]')"
echo Log.[a-z] occurances remaining: $(echo -n "$result" | wc -l) 
echo "$result"


