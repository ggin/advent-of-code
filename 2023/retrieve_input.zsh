year=2023
day=$1
cookie=53616c7465645f5f420d5844f2120602b58e25424f08a9a8a550ca84ee3276657faab57a2d36ad50bbcba083557b14074689fdf994f05e3f16581d13d5a7e7c9
curl -b "session=$cookie" https://adventofcode.com/$year/day/$day/input > src/main/resources/day$day-input.txt
