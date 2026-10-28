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
import { usePathname, useRouter } from "next/navigation";
import { useState } from "react";

type Props = {
  challengeId: number;
  participantId: number;
  disabled: boolean;
};

export function RemoveParticipantButton({
  challengeId,
  participantId,
  disabled,
}: Props) {
  const [open, setOpen] = useState(false);
  const router = useRouter();
  const pathname = usePathname();

  async function handleRemove() {
    const { error } = await removeParticipant(challengeId, participantId);

    if (error) {
      setOpen(false);
      toastProblemDetail(error);
    } else {
      // Close dialog and remove query param from url
      setOpen(false);
      router.push(pathname);
    }
  }

  return (
    <AlertDialog open={open} onOpenChange={setOpen}>
      <AlertDialogTrigger
        render={
          <Button variant="destructive" disabled={disabled}>
            Remove selected participant
          </Button>
        }
      />
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
