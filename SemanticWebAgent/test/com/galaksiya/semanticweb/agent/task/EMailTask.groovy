package com.galaksiya.semanticweb.agent.task;

import com.galaksiya.semanticweb.agent.GTask
import com.galaksiya.semanticweb.agent.Ontology

@Ontology(uri = "http://www.galaksiya.com/resource/ErdemsAgent",
	schema = "http://www.galaksiya/ontologies/communication.n3",
	location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/communication.n3")
public class EMailTask extends GTask{

	public EMailTask() {
		super(EMailTask.class.getSimpleName());
	}

	@Override
	public void execute() throws Exception {
		String reciever = "erdemeserekinci@gmail.com"
		String title = "Test Mail"
		String content = "This is the content of the test mail."
		Emailer emailer = new Emailer("er0891es80ek21");
		emailer.send("erdemeserekinci@galaksiya.com", reciever, title,
				content);
		this.model.createMessage {
			hasChannel("Mail")
			hasContent(content)
			hasTime(System.currentTimeMillis()+"")
			hasReciever(reciever)
		}
		this.model.write System.out
	}
}