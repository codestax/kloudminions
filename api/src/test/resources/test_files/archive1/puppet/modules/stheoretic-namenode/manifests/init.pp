

class namenode ($serverId) {

	package {'hadoop-hdfs-namenode':
	        ensure => installed
	}
	
	
	
	file {'/var/lib/zookeeper/myid' :
	        content => "${serverId}",
	        require => Package['zookeeper-server']
	}
	
	service {'zookeeper-server':
	        name => 'zookeeper-server',
	        ensure => 'running',
	        enable => true,
	        require => File['/var/lib/zookeeper/myid']
	}

}