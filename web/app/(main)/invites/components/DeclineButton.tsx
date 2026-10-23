"use client";

import { declineInvite } from "@/actions/invites";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { toastProblemDetail } from "@/lib/toast-utils";

type DeclineButtonProps = {
  inviteId: number;
};

export default function DeclineButton({ inviteId }: DeclineButtonProps) {
  async function handleClick() {
    const { success, error } = await declineInvite(inviteId);
    if (!success) {
      toastProblemDetail(error);
    }
  }

  return (
    <AlertDialog>
      <AlertDialogTrigger
        render={<Button variant="destructive">Decline</Button>}
      />
      <AlertDialogContent>
        <AlertDialogTitle>Are you sure?</AlertDialogTitle>
        <AlertDialogDescription>This can't be undone</AlertDialogDescription>
        <AlertDialogFooter>
          <AlertDialogAction variant="destructive" onClick={handleClick}>
            Decline
          </AlertDialogAction>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
