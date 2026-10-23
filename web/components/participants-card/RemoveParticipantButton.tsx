"use client";

import { removeParticipant } from "@/actions/participant-remove";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { toastProblemDetail } from "@/lib/toast-utils";
import { useState } from "react";

type RemoveParticipantButtonProps = {
  challengeId: number;
  participantId: number;
};

export default function RemoveParticipantButton({
  challengeId,
  participantId,
}: RemoveParticipantButtonProps) {
  const [open, setOpen] = useState(false);

  async function handleRemove() {
    const { error } = await removeParticipant(challengeId, participantId);

    if (error) {
      setOpen(false);
      toastProblemDetail(error);
    }
  }

  return (
    <AlertDialog open={open} onOpenChange={setOpen}>
      <AlertDialogTrigger render={<Button variant="outline">Remove</Button>} />
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Remove Participant</AlertDialogTitle>
          <AlertDialogDescription>
            Are you sure you want to remove this participant? All their data for
            this challenge will be lost. This cannot be undone.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction
            variant="destructive"
            onClick={handleRemove}
            render={<Button variant="outline">Remove</Button>}
          />
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
