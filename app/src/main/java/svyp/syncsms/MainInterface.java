package svyp.syncsms;

import svyp.syncsms.models.Message;

interface MainInterface {
    void archive(Message message);
    void unArchive(Message message);
}
