install:
	mysql -u root < schema.sql
	if [ -e scaffolding_data.sql ]; then ``; else perl generate_scaffolding_data.pl; fi 
	mysql -u root < scaffolding_data.sql

clean:
	mysql -u root < drop_database.sql
	rm scaffolding_data.sql
