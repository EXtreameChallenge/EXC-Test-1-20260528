// Backup ability placeholder for HarmonyOS 4.2
// This feature is not available in API version 11

export default class EntryBackupAbility {
  async onBackup() {
    console.info('onBackup ok');
    await Promise.resolve();
  }

  async onRestore(bundleVersion: Object) {
    console.info('onRestore ok');
    await Promise.resolve();
  }
}
