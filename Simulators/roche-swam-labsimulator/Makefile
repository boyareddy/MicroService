DEBEMAIL=$(shell git config user.email)
DEBFULLNAME=$(shell git config user.name)
all: compile


compile:
	@mvn package --offline -Dmaven.repo.local=/usr/share/maven-repo -Dmaven.test.skip=true


test:
	@mvn test --offline -Dmaven.repo.local=/usr/share/maven-repo


maven_clean:
	@mvn clean --offline -Dmaven.repo.local=/usr/share/maven-repo

deb:
	@debuild -us -uc

release:
	@export DEBFULLNAME=${DEBFULLNAME}
	@export DEBEMAIL=${DEBEMAIL}
	DEBFULLNAME="${DEBFULLNAME}" DEBEMAIL="${DEBEMAIL}" create-release 

