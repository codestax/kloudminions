

class zookeeper ($serverId) {

	package {'zookeeper':
	        ensure => installed
	}
	
	package {'zookeeper-server' :
	        ensure => installed,
	        require => Package['zookeeper']
	}
	
	file {'/var/lib/zookeeper/myid' :
	        content => "${serverId}"
	}
	
	service {'zookeeper-server':
	        name => 'zookeeper-server',
	        ensure => 'running',
	        enable => true
	}

}