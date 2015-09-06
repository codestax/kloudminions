package com.cm.minions

import com.cm.context.Context
import com.cm.context.ContextAware
import com.cm.executable.Executable


interface Minion extends Executable, ContextAware {

	public void execute(Context context)
	
}
