"use client";

import { declineInvite } from "@/actions/invites";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { toastGenericError } from "@/lib/toast-utils";

type DeclineButtonProps = {
  inviteId: number;
};

export default function DeclineButton({ inviteId }: DeclineButtonProps) {
  async function handleClick() {
    const { success } = await declineInvite(inviteId);
    if (!success) {
      toastGenericError();
    }
  }

  return (
    <Dialog>
      <DialogTrigger render={<Button variant="destructive">Decline</Button>} />
      <DialogContent>
        <DialogTitle>Are you sure?</DialogTitle>
        <DialogDescription>This can't be undone</DialogDescription>
        <DialogFooter>
          <Button variant="destructive" onClick={handleClick}>
            Decline
          </Button>
          <DialogClose render={<Button variant="secondary">Cancel</Button>} />
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
