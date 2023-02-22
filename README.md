PlayerController


Description:

		1. This application is about creating users and operations with them;
		
		2. In the system already created 2 uses with roles: ‘supervisor’ (this user cannot be
		deleted, only he can edit the user) and ‘admin’;
		
		3. Swagger is on the link http://3.68.165.45/swagger-ui.html
		
Basic application requirements:

		1. User should be older than 16 and younger than 60 years old;
		
		2. Only those with role ‘supervisor’ or ‘admin’ can create users;
		
		3. User can be created only with one role from the list: ‘admin’ or ‘user’
		
		4. ‘login’ field is unique for each user;
		
		5. ‘screenName’ field is unique for each user;
		
		6. ‘password’ must contain latin letters and numbers (min 7 max 15 characters);
		
		7. User`s ‘gender’ can only be: ‘male’ or ‘female’;
		
Required fields:

		/player/create/{editor}
				editor - the ‘login’ of the user who calls the method
				age
				gender
				login
				password
				role
				screenName
				
		/player/delete/{editor}
				editor - the ‘login’ of the user who calls the method
				playerId
		/player/get
				playerId
				
		/player/update/{editor}/{id}
				editor - the ‘login’ of the user who calls the method
				id - ‘id’ of user who need to be changed
				In this method, you can send any field from the following:
				age
				gender
				login
				password
				screenName
				
Role model:

supervisor:

	- can perform any operations in the system except for deleting users with the supervisor role
admin:

	- can perform any operations with users with the user and admin roles (if it is himself for admin role)
user:

	- can perform any operation on its user except delete
