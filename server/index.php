<?php

include "credentials.php";
include "GCMPushMessage.php";

$device = array($_GET["registered_id"]);
$message = $_GET["message"];

if (!device || ! $message) return;

$gcpm = new GCMPushMessage($apiKey);
$gcpm->setDevices($devices);
$response = $gcpm->send($message, array('title' => 'Test title'));

echo $response;
