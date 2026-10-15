"use client";

import { acceptInvite } from "@/actions/invites";
import { Button } from "@/components/ui/button";
import { toastProblemDetail } from "@/lib/toast-utils";
import { redirect } from "next/navigation";

type AcceptButtonProps = {
  inviteId: number;
  challengeId: number;
};

export default function AcceptButton({
  inviteId,
  challengeId,
}: AcceptButtonProps) {
  async function handleClick() {
    const { success, error } = await acceptInvite(inviteId);
    if (!success) {
      toastProblemDetail(error);
      return;
    }
    redirect(`/challenges/${challengeId}`);
  }
  return <Button onClick={handleClick}>Accept</Button>;
}
