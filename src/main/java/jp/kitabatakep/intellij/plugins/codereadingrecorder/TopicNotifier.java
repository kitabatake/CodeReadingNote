package jp.kitabatakep.intellij.plugins.codereadingrecorder;

public interface TopicNotifier
{
    com.intellij.util.messages.Topic<TopicNotifier> TOPIC_NOTIFIER_TOPIC =
        com.intellij.util.messages.Topic.create("topic notifier", TopicNotifier.class);

    void lineDeleted(Topic topic, TopicLine topicLine);
    void lineAdded(Topic topic, TopicLine topicLine);
}
