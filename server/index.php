include "credentials.php";
include "GCMPushMessage.php";
$devices = array('YOUR REGISTERED DEVICE ID');
$message = "The message to send";

$gcpm = new GCMPushMessage($apiKey);
$gcpm->setDevices($devices);
$response = $gcpm->send($message, array('title' => 'Test title'));

echo $response;