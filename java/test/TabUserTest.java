package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import skaipeuh.TabUser;

public class TabUserTest {

	TabUser tabUser;

	@Before
	public void setUp() {
		tabUser = new TabUser();
	}

	@Test
	public void addAndRemoveUser() {
		tabUser = new TabUser();

		tabUser.addUser("IAM user    ");
		tabUser.removeUser("user    ");

		assertTrue(tabUser.getNbEle() == 0);
	}
}
