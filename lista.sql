
create database lista;

use lista;

CREATE TABLE `desejos` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `desejo` varchar(255) NOT NULL,
  `prioridade` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



INSERT INTO `desejos` (`id`, `name`, `desejo`, `prioridade`) VALUES
(NULL, 'Murilo', 'Ter uma Casa Própria', 'Alta'),
(NULL, 'Kayo', 'Ser Barbudão', 'Média');

