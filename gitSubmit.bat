@echo off
rem 在提交项目代码或者拉代码的时候，git会让你输入用户名密码，解决方案：（我们公司用的是gitlab）
rem 执行git config --global credential.helper store命令,提交后会让输入一次，之后不用再输入
set cd=%~dp0
cd %cd%

set proFile=index
set key=version
set version=
for /f %%i in ('findstr "^%key%" %proFile%') do set version=%%i
set /a verNum=%version:~8% + 1
echo version=%verNum% > %proFile%

git add .
git commit -m "%verNum% , %DATE% %TIME%"

git fetch origin master
git rebase

git push

pause