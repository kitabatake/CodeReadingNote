package jp.kitabatakep.intellij.plugins.codereadingrecorder;

public interface TopicListNotifier
{
    com.intellij.util.messages.Topic<TopicListNotifier> TOPIC_LIST_NOTIFIER_TOPIC =
        com.intellij.util.messages.Topic.create("topic list notifier", TopicListNotifier.class);

     void topicAdded(Topic topic);
     void topicDeleted(Topic topic);
}
