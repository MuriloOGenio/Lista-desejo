<?php 


	require_once '../includes/DbOperation.php';

	function isTheseParametersAvailable($params){
	
		$available = true; 
		$missingparams = ""; 
		
		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}
		
		
		if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
			
		
			echo json_encode($response);
			
		
			die();
		}
	}
	
	
	$response = array();
	

	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
	
			case 'createdesejo':
				
				isTheseParametersAvailable(array('name','desejo','prioridade'));
				
				$db = new DbOperation();
				
				$result = $db->createDesejo(
					$_POST['name'],
					$_POST['desejo'],
					$_POST['prioridade']
				);
				

			
				if($result){
					
					$response['error'] = false; 

					
					$response['message'] = 'Seu Desejo Foi Adicionado';

					
					$response['desejos'] = $db->getDesejos();
				}else{

					
					$response['error'] = true; 

				
					$response['message'] = 'Algum erro ocorreu por favor tente novamente';
				}
				
			break; 
			
		
			case 'getdesejos':
				$db = new DbOperation();
				$response['error'] = false; 
				$response['message'] = 'Pedido concluído com sucesso';
				$response['desejos'] = $db->getDesejos();
			break; 
			
			
		
			case 'updatedesejo':
				isTheseParametersAvailable(array('id','name','desejo','prioridade'));
				$db = new DbOperation();
				$result = $db->updateDesejo(
					$_POST['id'],
					$_POST['name'],
					$_POST['desejo'],
					$_POST['prioridade']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Seu Desejo Foi Alterado';
					$response['desejos'] = $db->getDesejos();
				}else{
					$response['error'] = true; 
					$response['message'] = 'Algum erro ocorreu por favor tente novamente';
				}
			break; 
			
			
			case 'deletedesejo':

				
				if(isset($_GET['id'])){
					$db = new DbOperation();
					if($db->deleteDesejo($_GET['id'])){
						$response['error'] = false; 
						$response['message'] = 'Desejo excluído com sucesso';
						$response['desejos'] = $db->getDesejos();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Algum erro ocorreu por favor tente novamente';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Não foi possível deletar, forneça um id por favor';
				}
			break; 
		}
		
	}else{
		 
		$response['error'] = true; 
		$response['message'] = 'Chamada de API inválida';
	}
	

	echo json_encode($response);
	
	
