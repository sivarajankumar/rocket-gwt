/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.remoting.server.comet;

import rocket.remoting.client.CometConstants;

/**
 * Instances of this class represent a request from the server to terminates an
 * active comet session.
 * 
 * @author Miroslav Pokorny
 */
public class Terminate implements Message {

	public Terminate( final long sequence ){
		super();
		this.setSequence(sequence);
	}
	
	public int getCommand() {
		return CometConstants.TERMINATE_COMET_SESSION;
	}

	public Object getObject() {
		return null;
	}

private long sequence;
	
	public long getSequence(){
		return this.sequence;
	}
	
	void setSequence( final long sequence ){
		this.sequence = sequence;
	}
}
