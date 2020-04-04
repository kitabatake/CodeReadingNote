package jp.kitabatakep.intellij.plugins.codereadingnote;

public interface TopicNotifier
{
    com.intellij.util.messages.Topic<TopicNotifier> TOPIC_NOTIFIER_TOPIC =
        com.intellij.util.messages.Topic.create("topic notifier", TopicNotifier.class);

    void lineRemoved(Topic topic, TopicLine topicLine);
    void lineAdded(Topic topic, TopicLine topicLine);
}
