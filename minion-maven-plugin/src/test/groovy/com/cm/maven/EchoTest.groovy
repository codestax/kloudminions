package com.cm.maven


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

class EchoTest {

	@Test
	void testApply_WhenSendingAMessage_ExpectTheMessageToBeRepeated() throws Exception {
		String expectedMessage = "foo"
		
		String actualMessage = new Echo().apply(expectedMessage)
		
		assertThat(actualMessage, is(equalTo(expectedMessage)))
	}
}
