package com.cm.providers.aws

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.CreateImageRequest
import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.google.common.collect.ImmutableSet
import com.google.common.collect.TreeMultiset
import com.google.common.collect.Multiset.Entry

class AMIMinion {

    //TODO: Instantiate with retry policy
    AmazonEC2Client amazonEC2Client = new AmazonEC2Client()

    public AMIMinion() {

    }

    def createImage(def describeInstancesResult){

        def transitions = new ArrayList<String>()

        for (def reservation : describeInstancesResult.getReservation())
        {
            for (def instance : reservation.getInstances())
            {

                def resourceId = instance.getInstanceId()
                def dateTime = new DateTime().toString(ISODateTimeFormat.basicDateTimeNoMillis().withZone(DateTimeZone.UTC))
                def amiName = resourceId + "." + dateTime

                def createImageRequest = new CreateImageRequest().withInstanceId(resourceId)
                        .withName(amiName)

                def createImageResult = amazonEC2Client.createImage(createImageRequest)

                def imageId = createImageResult.getImageId()

                println "Creating image '" + imageId + "' of '" + resourceId


                transitions.add(imageId)
            }
        }

        waitForTransitionCompletion(transitions)

        return transitions
    }

    def waitForTransitionCompletion(def transitions){

        def imageStateCompleteSet = ImmutableSet.<String> builder()
                .add("available", "deregistered", "failed").build()

        def describeImagesRequest = new DescribeImagesRequest().withImageIds(transitions)
        def imageStates = TreeMultiset.create()
        def resourceStates = new HashMap<String, String>()

        def transitionCompleted = false

        while (!transitionCompleted)
        {
            imageStates.clear()
            try
            {
                def describeImagesResult = amazonEC2Client.describeImages(describeImagesRequest)
                def images = describeImagesResult.getImages()
                for (def image : images)
                {
                    imageStates.add(image.getState())
                    resourceStates.put(image.getImageId(), image.getState())
                }
            }
            catch (AmazonServiceException ase)
            {
                println "Failed to describe images!"
                ase.printStackTrace()
            }

            transitionCompleted = imageStateCompleteSet.containsAll(imageStates)

            // Sleep until transition has completed.
            if (!transitionCompleted)
            {
                String entryStatus = this.formatTransitionResult(imageStates)

                "... image(s) still transitioning (" + entryStatus + ")"
                sleep(3000)
            }
        }

        String entryStatus = this.formatTransitionResult(imageStates)
        println "... images transitioned (" + entryStatus + ")."

        return imageStates
    }
    def formatTransitionResult(def transitionResult)
    {
        def entryStatus = ""
        if (0 < transitionResult.size())
        {
            for (Entry<String> entry : transitionResult.entrySet())
            {
                entryStatus += entry.getElement() + ": " + entry.getCount() + ", "
            }
            entryStatus.replaceAll(",\\s\$", "")
        }
        else
        {
            entryStatus += "none - not configured correctly or no longer available"
        }

        return entryStatus
    }

}
