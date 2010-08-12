WEB_INSTALL = /Library/WebServer/Documents/garth/

#####################
## install: default target
## Creates appropriate directories if they don't exist
## Copies what files have changed
#####################
install: index.php style.css includes/DbConnector.php
	if [ -d $(WEB_INSTALL) ]; then ``; else mkdir $(WEB_INSTALL); fi
	if [ -d $(WEB_INSTALL)/includes/ ]; then ``; else mkdir $(WEB_INSTALL)/includes/; fi 
	cp index.php $(WEB_INSTALL)
	cp style.css $(WEB_INSTALL)
	cp includes/DbConnector.php $(WEB_INSTALL)/includes/
