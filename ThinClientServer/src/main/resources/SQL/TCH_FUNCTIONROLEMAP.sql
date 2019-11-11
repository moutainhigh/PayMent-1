CREATE TABLE "TCH"."TCH_FUNCTIONROLEMAP" 
   (	"FUNCTIONID" NUMBER(10,0) NOT NULL ENABLE, 
	"ROLEID" NUMBER(10,0) NOT NULL ENABLE, 
	 CONSTRAINT "PK_FRM_COMP" PRIMARY KEY ("FUNCTIONID", "ROLEID"), 
	 CONSTRAINT "FK_FR_FUNCTIONID" FOREIGN KEY ("FUNCTIONID")
	  REFERENCES "TCH"."TCH_FUNCTIONS" ("ID") ENABLE, 
	 CONSTRAINT "FK_FR_ROLEID" FOREIGN KEY ("ROLEID")
	  REFERENCES "TCH"."TCH_ROLES" ("ID") ENABLE
   );