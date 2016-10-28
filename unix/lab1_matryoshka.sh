# first option 
touch file 
dd if=/dev/zero of=file bs=1M count=512 
mke2fs -t ext2 file 
mkdir mountingdir 
sudo mount -t ext2 -o loop file mountingdir 

# $ cat /proc/mounts 
#/dev/loop0 /home/vadim/test/mountingdir ext2 rw,relatime,block_validity,barrier,user_xattr,acl 0 0 <- новая запись 

sudo chown -R vadim mountingdir 
cd mountingdir 
mkdir aa bb 


# second option: sparse-file 
touch file-sparse 
dd of=file-sparse bs=1 seek=512M count=0 # file-sparse apparent-size=512M, actual-size=0M 
mke2fs -t ext2 file-sparse # file-sparse apparent-size=512M, actual-size=8.4M <- после создания ФС реальный размер файла увеличился 
mkdir mountingdir-sparse 
sudo mount -t ext2 -o loop /home/vadim/test/file-sparse /home/vadim/test/mountingdir-sparse 

# $ cat /proc/mounts 
#/dev/loop1 /home/vadim/test/mountingdir-sparse ext2 rw,relatime,block_validity,barrier,user_xattr,acl 0 0 <- новая запись 

sudo chown -R vadim mountingdir-sparse 
cd mountingdir-sparse 
mkdir aa bb 
dd if=/dev/zero of=/home/vadim/test/mountingdir-sparse/aa/two_megabytes bs=1024 count=2048 # file-sparse apparent-size=512M, actual-size=10.4M <- файл two_megabytes добавил 2 МБ в file-sparse 


# также использовал 
# du -s dir 
# du -s -m --apparent-size dir