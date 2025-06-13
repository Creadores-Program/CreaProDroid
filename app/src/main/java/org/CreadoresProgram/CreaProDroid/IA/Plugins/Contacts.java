package org.CreadoresProgram.CreaProDroid.IA.Plugins;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.content.Context;
public class Contacts extends PluginIA{
    @Override
    public String getInfo(){
        StringBuilder contactsInfo = new StringBuilder();
        Cursor cursor = this.context.getContentResolver().query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null);
        if (cursor != null) {
            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                contactsInfo.append("Nombre: ").append(name);
                Cursor phones = this.context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone._ID + " = ?",
                    new String[]{id}, null);
                if (phones != null && phones.moveToFirst()) {
                    do {
                        String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactsInfo.append("\nTel: ").append(phone);
                    }while (phones.moveToNext());
                    phones.close();
                }
                Cursor emails = this.context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{id}, null);
                if (emails != null && emails.moveToFirst()) {
                    do {
                        String email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                        contactsInfo.append("\nEmail: ").append(email);
                    } while (emails.moveToNext());
                    emails.close();
                }
            }
            cursor.close();
        }
        return "[Contacts] " + (contactsInfo.toString());
    }
}