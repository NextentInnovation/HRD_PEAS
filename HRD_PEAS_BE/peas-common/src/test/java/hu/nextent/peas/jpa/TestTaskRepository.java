package hu.nextent.peas.jpa;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import hu.nextent.peas.jpa.config.HibernateConfig;
import hu.nextent.peas.jpa.config.HibernateTestConfig;
import hu.nextent.peas.jpa.dao.TaskRepository;
import hu.nextent.peas.jpa.entity.Task;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( classes = { HibernateTestConfig.class, HibernateConfig.class }, loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class TestTaskRepository {

	@Resource
	private TaskRepository taskRepository;

	@Test
	public void givenUser() {
		List<Task> tasks = taskRepository.findAll();
		System.out.print(tasks.get(0));
	}

}
