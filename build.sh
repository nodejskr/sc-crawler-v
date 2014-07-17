
cd mod-shopcrawler-infomanager

./gradlew

cd ..

cd mods

rm -rf ./*.class

cd ..

cp -rf ./mod-shopcrawler-infomanager/build/mods/* ./mods/

