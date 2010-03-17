digit    = \d*\.?\d+
id       = [a-z]+
constant = [A-Z]+
func     = [a-z]+[(] 

power = [^]
plus  = [+]
minus = [-]
mult  = [*]
div   = [/]
mod   = [%]

lp    = [(]
rp    = [)]




# START: lp START rp 
#	 | START + START 
#	 | START * START 
#	 | START START 
#	 | START / START 
#	 | START mod START
#	 | id | digit | constant
	 	 
	 
START  :  PUNKT STRICH_  
STRICH_ :  plus START | minus STRICH_ | €

PUNKT   :  VALUE PUNKT_
PUNKT_  :  div PUNKT | mult PUNKT | mod PUNKT  | €


VALUE   : minus POW | POW
POW     : VALUE_ POW_ 
POW_    : power START | €   

VALUE_  : id | digit | constant | lp START rp | FUNC

FUNC    : func START rp
