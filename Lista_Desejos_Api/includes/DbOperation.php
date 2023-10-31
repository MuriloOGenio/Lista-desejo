<?php
 
class DbOperation
{
    
    private $con;
 
 
    function __construct()
    {
  
        require_once dirname(__FILE__) . '/DbConnect.php';
 
     
        $db = new DbConnect();
 

        $this->con = $db->connect();
    }
	
	
	function createDesejo($name, $desejo, $prioridade){
		$stmt = $this->con->prepare("INSERT INTO desejos (name, desejo, prioridade) VALUES (?, ?, ?)");
		$stmt->bind_param("sss", $name, $desejo, $prioridade);
		if($stmt->execute())
			return true; 
		return false; 
	}
		
	function getDesejos(){
		$stmt = $this->con->prepare("SELECT id, name, desejo, prioridade FROM desejos");
		$stmt->execute();
		$stmt->bind_result($id, $name, $desejo, $prioridade);
		
		$desejos = array(); 
		
		while($stmt->fetch()){
			$desejo = array();
			$desejo['id'] = $id; 
			$desejo['name'] = $name; 
			$desejo['desejo'] = $desejo; 
			$desejo['prioridade'] = $prioridade; 
			
			array_push($desejos, $desejo); 
		}
		
		return $desejos; 
	}
	
	
	function updateDesejo($id, $name, $desejo, $prioridade){
		$stmt = $this->con->prepare("UPDATE desejos SET name = ?, desejo = ?, prioriadade = ? WHERE id = ?");
		$stmt->bind_param("sssi", $name, $desejo, $prioridade, $id);
		if($stmt->execute())
			return true; 
		return false; 
	}
	
	
	function deleteDesejo($id){
		$stmt = $this->con->prepare("DELETE FROM desejos WHERE id = ? ");
		$stmt->bind_param("i", $id);
		if($stmt->execute())
			return true; 
		
		return false; 
	}
}