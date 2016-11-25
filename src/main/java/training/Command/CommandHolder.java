package training.Command;

public interface CommandHolder {

	Command get(String path);

	Command post(String path);

	void init();
}
