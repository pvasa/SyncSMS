package svyp.syncsms;

import svyp.syncsms.models.Conversation;

interface MainInterface {
    void archive(Conversation conversation);
    void unArchive(Conversation conversation);
}
