package shishkin.cleanarchitecture.note.mail;

import com.cleanarchitecture.common.utils.ApplicationUtils;
import com.cleanarchitecture.sl.mail.AbsMail;
import com.cleanarchitecture.sl.sl.MailSubscriber;


import shishkin.cleanarchitecture.note.data.Note;
import shishkin.cleanarchitecture.note.screen.notes.NotesPresenter;

public class OnChangeNoteMail extends AbsMail {

    private static final String NAME = OnChangeNoteMail.class.getName();

    public OnChangeNoteMail() {
        super(NotesPresenter.NAME);
    }

    @Override
    public void read(final MailSubscriber subscriber) {
        ApplicationUtils.runOnUiThread(() -> {
            if (OnDataChangeListener.class.isInstance(subscriber))
                ((OnDataChangeListener) subscriber).onChange(Note.TABLE);
        });
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isCheckDublicate() {
        return true;
    }

}
