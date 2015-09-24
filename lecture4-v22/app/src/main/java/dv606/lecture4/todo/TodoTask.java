package dv606.lecture4.todo;

public class TodoTask {
	  private long id;
	  private String task;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getTask() {
	    return task;
	  }

	  public void setTask(String task) {
	    this.task = task;
	  }

	  @Override
	  public String toString() {
	    return task;
	  }
}
