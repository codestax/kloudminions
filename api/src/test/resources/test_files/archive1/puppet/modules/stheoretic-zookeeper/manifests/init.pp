

class zookeeper ($serverId) {

	package {'zookeeper':
	        ensure => installed
	}
	
	package {'zookeeper-server' :
	        ensure => installed,
	        require => Package['zookeeper']
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