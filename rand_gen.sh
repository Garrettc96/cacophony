#!/bin/bash

chars='abcdefghijklmnopqrstuvwxyz'
n=128

str=
for ((i = 0; i < n; ++i)); do
    str+=${chars:RANDOM%${#chars}:1}
    # alternatively, str=$str${chars:RANDOM%${#chars}:1} also possible
done

echo "$str"
